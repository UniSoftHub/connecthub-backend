package br.com.hub.connect.application.communication.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.communication.dto.CreateNotificationDTO;
import br.com.hub.connect.application.communication.dto.NotificationResponseDTO;
import br.com.hub.connect.domain.communication.model.Notification;
import br.com.hub.connect.domain.exception.NotificationNotFoundException;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import br.com.hub.connect.domain.user.model.User;
import io.smallrye.common.constraint.NotNull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class NotificationService {

  public List<NotificationResponseDTO> findAll(int page, int size) {
    return Notification.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public NotificationResponseDTO findById(Long id) {
    Notification notification = Notification.findActiveById(id)
        .orElseThrow(() -> new NotificationNotFoundException(id));

    return toResponseDTO(notification);
  }

  @Transactional
  public NotificationResponseDTO create(@Valid CreateNotificationDTO dto) {

    Notification notification = new Notification();
    notification.type = dto.type();
    notification.title = dto.title();
    notification.message = dto.message();
    notification.referenceId = dto.referenceId();
    notification.user = User.findActiveById(dto.userId())
        .orElseThrow(() -> new UserNotFoundException(dto.userId()));
    notification.persist();

    return toResponseDTO(notification);

  }

  //
  // @Transactional
  // public NotificationResponseDTO update(@NotNull Long id, @Valid
  // UpdateNotificationDTO dto) {
  // Notification notification = Notification.findActiveById(id)
  // .orElseThrow(() -> new NotificationNotFoundException(id));
  //
  // if (dto.type() != null) {
  // notification.type = dto.type();
  // }
  // if (dto.title() != null) {
  // notification.title = dto.title();
  // }
  // if (dto.message() != null) {
  // notification.message = dto.message();
  // }
  // if (dto.referenceId() != null) {
  // notification.referenceId = dto.referenceId();
  // }
  // if (dto.user() != null) {
  // notification.user = dto.user();
  // }
  //
  // return toResponseDTO(notification);
  // }
  //

  @Transactional
  public void delete(@NotNull Long id) {
    Notification notification = Notification.findActiveById(id)
        .orElseThrow(() -> new NotificationNotFoundException(id));
    notification.softDelete();
    notification.persist();
  }

  public long count() {
    return Notification.countActive();
  }

  private NotificationResponseDTO toResponseDTO(Notification notification) {
    return new NotificationResponseDTO(
        notification.id,
        notification.type,
        notification.title,
        notification.message,
        notification.referenceId,
        notification.user.id);
  }

}
