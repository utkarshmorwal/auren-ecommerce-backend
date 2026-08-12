package com.ecommerce.service;

import com.ecommerce.dto.NotificationDTO;
import com.ecommerce.model.Notification;
import com.ecommerce.model.Order;
import com.ecommerce.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void createForOrder(Order order) {
        Notification notification = new Notification();
        notification.setOrderId(order.getId());
        notification.setCustomerName(order.getUser().getName());
        notification.setTotalAmount(order.getTotalAmount());
        notification.setRead(false);
        Notification saved = notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications", convertToDTO(saved));
    }

    public List<NotificationDTO> getRecent() {
        return notificationRepository.findTop20ByOrderByCreatedAtDesc()
                .stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public long getUnreadCount() {
        return notificationRepository.countByReadFalse();
    }

    public void markAsRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        n.setRead(true);
        notificationRepository.save(n);
    }

    public void markAllAsRead() {
        List<Notification> unread = notificationRepository.findAll().stream()
                .filter(n -> !n.getRead())
                .collect(Collectors.toList());
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
    }

    private NotificationDTO convertToDTO(Notification n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(n.getId());
        dto.setOrderId(n.getOrderId());
        dto.setCustomerName(n.getCustomerName());
        dto.setTotalAmount(n.getTotalAmount());
        dto.setRead(n.getRead());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}