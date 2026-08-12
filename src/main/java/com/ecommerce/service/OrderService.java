package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderItemDTO;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PromoService promoService;
    
    @Autowired
    private NotificationService notificationService;

    public OrderDTO createOrder(OrderDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        order.setShippingName(dto.getShippingName());
        order.setShippingPhone(dto.getShippingPhone());
        order.setShippingAddress(dto.getShippingAddress());
        order.setShippingCity(dto.getShippingCity());
        order.setShippingState(dto.getShippingState());
        order.setShippingPostalCode(dto.getShippingPostalCode());
        order.setRazorpayOrderId(dto.getRazorpayOrderId());
        order.setRazorpayPaymentId(dto.getRazorpayPaymentId());
        order.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : "PAID");
        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;

        for (OrderItemDTO itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
            subtotal += product.getPrice() * itemDto.getQuantity();
        }

        double discountPercent = promoService.getDiscountPercent(dto.getPromoCode(), subtotal);
        double discountAmount = subtotal * discountPercent;
        double total = subtotal - discountAmount;

        order.setItems(orderItems);
        order.setSubtotal(subtotal);
        order.setPromoCode(dto.getPromoCode());
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        notificationService.createForOrder(saved);
        return convertToDTO(saved);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return convertToDTO(updated);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private OrderDTO convertToDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        OrderDTO dto = new OrderDTO(
                order.getId(),
                order.getUser().getId(),
                itemDTOs,
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
        dto.setSubtotal(order.getSubtotal());
        dto.setPromoCode(order.getPromoCode());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setShippingName(order.getShippingName());
        dto.setShippingPhone(order.getShippingPhone());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingState(order.getShippingState());
        dto.setShippingPostalCode(order.getShippingPostalCode());
        dto.setRazorpayOrderId(order.getRazorpayOrderId());
        dto.setRazorpayPaymentId(order.getRazorpayPaymentId());
        dto.setPaymentStatus(order.getPaymentStatus());
        return dto;
    }
}