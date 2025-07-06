package ar.edu.uade.desa1.service;

import ar.edu.uade.desa1.domain.entity.DeliveryRoute;
import ar.edu.uade.desa1.domain.entity.User;
import ar.edu.uade.desa1.domain.enums.RouteStatus;
import ar.edu.uade.desa1.domain.request.CreateRouteRequest;

import ar.edu.uade.desa1.domain.request.UpdateRouteStatusRequest;

import ar.edu.uade.desa1.domain.response.DeliveryRouteResponse;
import ar.edu.uade.desa1.exception.NotFoundException;
import ar.edu.uade.desa1.repository.DeliveryRouteRepository;
import ar.edu.uade.desa1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;


import ar.edu.uade.desa1.service.FirebaseMessagingService;
import ar.edu.uade.desa1.service.TokenStorageService;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.uade.desa1.exception.BadRequestException;



import java.util.Random;


import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class DeliveryRouteServiceImpl implements DeliveryRouteService {

    private final DeliveryRouteRepository deliveryRouteRepository;
    private final UserRepository userRepository;

    private final TokenStorageService tokenStorageService;
    private final FirebaseMessagingService firebaseMessagingService;

    private final Random random = new Random();

    @Override
    @Transactional
    public DeliveryRoute createRoute(CreateRouteRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found for id: " + request.getUserId()));
        if (user.getRole() == null || user.getRole().getId() != 2) {
            throw new BadRequestException("You can only create a route for users with role 'usuario'");
        }


        try {
            DeliveryRoute route = DeliveryRoute.builder()
                    .packageInfo(request.getPackageInfo())
                    .origin(request.getOrigin())
                    .destination(request.getDestination())
                    .status(request.getStatus())
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build();


            DeliveryRoute savedRoute = deliveryRouteRepository.save(route);

            
            // Notificar si la ruta está disponible
            if (route.getStatus() == RouteStatus.AVAILABLE) {
                 

                String token = tokenStorageService.getAnyAvailableDeliveryToken();
                  
                if (token != null) {
                    try {
                        firebaseMessagingService.sendNotification(
                                "Nueva ruta disponible",
                                "Hay una entrega esperando ser tomada",
                                token, "Shipments"
                        );
                    } catch (Exception e) {
                        System.err.println("Error enviando notificación FCM: " + e.getMessage());
                    }
                }
            }

            return savedRoute;


        } catch (Exception e) {
            throw new RuntimeException("Error creating route: " + e.getMessage());
        }
    }


    @Override
    public List<DeliveryRouteResponse> getAllRoutes() {
        try {
            var routes = deliveryRouteRepository.findAll();

            return routes.stream().filter(r -> r.getDeliveryUser() == null).map(route -> DeliveryRouteResponse.builder()
                    .id(route.getId()).
                    userInfo(route.getUser().getFirstName() + " " + route.getUser().getLastName())
                    .packageInfo(route.getPackageInfo())
                    .origin(route.getOrigin())
                    .destination(route.getDestination())
                    .createdAt(route.getCreatedAt())
                    .updatedAt(route.getUpdatedAt())
                    .status(route.getStatus())
                    .build()).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error getting all routes: " + e.getMessage());
        }
    }


    @Override
    public List<DeliveryRouteResponse> getAllRoutesByUserId(Long userId) {
        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
            
            boolean isRegularUser = user.getRole().getName().equalsIgnoreCase("USUARIO");

            var routes = deliveryRouteRepository.findByUserId(userId);

            return routes.stream().map(route -> {
                var deliveryUserInfo = route.getDeliveryUser();
                return DeliveryRouteResponse.builder()
                    .id(route.getId())
                    .userInfo(route.getUser().getFirstName() + " " + route.getUser().getLastName())
                    .packageInfo(route.getPackageInfo())
                    .deliveryUserInfo(deliveryUserInfo != null ? deliveryUserInfo.getFirstName() + " " + deliveryUserInfo.getLastName() : null)
                    .origin(route.getOrigin())
                    .destination(route.getDestination())
                    .createdAt(route.getCreatedAt())
                    .updatedAt(route.getUpdatedAt())
                    .status(route.getStatus())

                    .completionCode(isRegularUser ? route.getCompletionCode() : null)
                    .build();
            }).toList();

        } catch (Exception e) {
            throw new RuntimeException("Error getting all routes for user: " + e.getMessage());
        }
    }

    @Override
    public DeliveryRoute getRouteById(Long id) {
        try {
            return deliveryRouteRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Route with id " + id + " not found"));
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting route by id: " + e.getMessage());
        }
    }

    
    private String generateRandomCode() {
        int code = 100000 + random.nextInt(900000); // Generates a number between 100000 and 999999
        return String.valueOf(code);
    }
    
    @Override
    @Transactional
    public DeliveryRouteResponse updateRouteStatus(UpdateRouteStatusRequest request) {
        try {
            User deliveryUser = userRepository.findById(request.getDeliveryRouteId())
                    .orElseThrow(() -> new NotFoundException("Delivery user not found for id: " + request.getDeliveryUserId()));

            DeliveryRoute route = deliveryRouteRepository.findById(request.getDeliveryRouteId())
                    .orElseThrow(() -> new NotFoundException("Route with id " + request.getDeliveryRouteId() + " not found"));

            String oldOrigin = route.getOrigin();
            String oldDestination = route.getDestination();

            // Si vienen valores nuevos en la request, actualizarlos
            if (request.getOrigin() != null) {
                route.setOrigin(request.getOrigin());
            }

            if (request.getDestination() != null) {
                route.setDestination(request.getDestination());
            }

            if (route.getDeliveryUser() != null && !route.getDeliveryUser().getId().equals(request.getDeliveryUserId())) {
                throw new RuntimeException("Only the assigned delivery user can change the route status");
            }

            RouteStatus newStatus = RouteStatus.valueOf(request.getStatus());
            
            if (RouteStatus.COMPLETED == newStatus) {
                if (request.getCompletionCode() == null || !request.getCompletionCode().equals(route.getCompletionCode())) {
                    throw new RuntimeException("Código de entrega inválido. Verificar con el destinatario.");
                }
                route.setCompletionCode(null);
            }
            
            // Generate random code when status changes to IN_PROGRESS
            if (RouteStatus.IN_PROGRESS == newStatus) {
                route.setDeliveryUser(deliveryUser);
                route.setCompletionCode(generateRandomCode());
            } 
            
            route.setStatus(newStatus);
            route.setUpdatedAt(LocalDateTime.now());
            DeliveryRoute savedRoute = deliveryRouteRepository.save(route);

            // Comparar cambios
            boolean originChanged = oldOrigin != null && !oldOrigin.equals(savedRoute.getOrigin());
            boolean destinationChanged = oldDestination != null && !oldDestination.equals(savedRoute.getDestination());

            if (savedRoute.getStatus() == RouteStatus.IN_PROGRESS &&
                    savedRoute.getDeliveryUser() != null &&
                    (originChanged || destinationChanged)) {

                String token = tokenStorageService.getToken(savedRoute.getDeliveryUser().getId().toString());
                if (token != null) {
                    try {
                        firebaseMessagingService.sendNotification(
                                "¡Ruta actualizada!",
                                "Se modificó el origen o destino de tu entrega.",
                                token, "MyRoutes"
                        );
                    } catch (Exception e) {
                        System.err.println("Error enviando notificación de cambio de ruta: " + e.getMessage());
                    }
                } else {
                    System.out.println("Repartidor sin token registrado.");
                }
            }

            return DeliveryRouteResponse.builder()
                    .id(savedRoute.getId())
                    .packageInfo(savedRoute.getPackageInfo())
                    .origin(savedRoute.getOrigin())
                    .destination(savedRoute.getDestination())
                    .status(savedRoute.getStatus())
                    .userInfo(savedRoute.getUser().getFirstName() + " " + savedRoute.getUser().getLastName())
                    .deliveryUserInfo(savedRoute.getDeliveryUser() != null ? savedRoute.getDeliveryUser().getFirstName() + " " + savedRoute.getDeliveryUser().getLastName(): null)
                    .createdAt(savedRoute.getCreatedAt())
                    .updatedAt(savedRoute.getUpdatedAt())
                    .build();

        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating route status: " + e.getMessage());
        }
    }



    @Override
    public List<DeliveryRouteResponse> getCompletedRoutesByUser(Long userId) {
        try {

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
                    

            List<DeliveryRoute> routes = deliveryRouteRepository.findByUserIdAndStatus(userId, RouteStatus.COMPLETED.toString());

            return routes.stream().map(route -> DeliveryRouteResponse.builder()
                    .id(route.getId())
                    .userInfo(route.getUser().getFirstName() + " " + route.getUser().getLastName())
                    .packageInfo(route.getPackageInfo())
                    .origin(route.getOrigin())
                    .destination(route.getDestination())
                    .createdAt(route.getCreatedAt())
                    .updatedAt(route.getUpdatedAt())
                    .status(route.getStatus())
                    .build()).toList();

        } catch (Exception e) {
            throw new RuntimeException("Error getting completed routes by user: " + e.getMessage());
        }
    }

    @Override
    public List<DeliveryRouteResponse> getAllRoutesByDeliveryUserId(Long deliveryUserId) {
        try {
            var routes = deliveryRouteRepository.findByDeliveryUserId(deliveryUserId);


            return routes.stream()
                    .filter(route -> RouteStatus.IN_PROGRESS.equals(route.getStatus()))
                    .map(route -> DeliveryRouteResponse.builder()
                            .id(route.getId())
                            .userInfo(route.getUser().getFirstName() + " " + route.getUser().getLastName())
                            .packageInfo(route.getPackageInfo())
                            .origin(route.getOrigin())
                            .destination(route.getDestination())
                            .createdAt(route.getCreatedAt())
                            .updatedAt(route.getUpdatedAt())
                            .status(route.getStatus())
                            .build()).toList();

        } catch (Exception e) {
            throw new RuntimeException("Error getting all routes for delivery user: " + e.getMessage());
        }    
  }
}
