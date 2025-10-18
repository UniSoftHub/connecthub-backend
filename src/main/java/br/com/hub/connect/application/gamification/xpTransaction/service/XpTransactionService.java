package br.com.hub.connect.application.gamification.xpTransaction.service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.hub.connect.application.gamification.xpTransaction.dto.CreateXpTransactionDTO;
import br.com.hub.connect.application.gamification.xpTransaction.dto.ResponseXpTransactionDTO;
import br.com.hub.connect.application.gamification.xpTransaction.dto.UpdateXpTransactionDTO;
import br.com.hub.connect.domain.exception.UserNotFoundException;
import br.com.hub.connect.domain.exception.XpTransactionNotFoundException;
import br.com.hub.connect.domain.gamification.model.XpTransaction;
import br.com.hub.connect.domain.user.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class XpTransactionService {

  public List<ResponseXpTransactionDTO> findAll(int page, int size) {
    return XpTransaction.findAllActive(page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public ResponseXpTransactionDTO findById(@NotNull Long id) {
    XpTransaction xpTransaction = XpTransaction.findActiveById(id)
        .orElseThrow(() -> new XpTransactionNotFoundException(id));

    return toResponseDTO(xpTransaction);
  }

  @Transactional
  public ResponseXpTransactionDTO create(@Valid CreateXpTransactionDTO dto) {
    User user = User.findActiveById(dto.userId())
        .orElseThrow(() -> new UserNotFoundException(dto.userId()));

    if (XpTransaction.existsByReferenceIdAndTypeActive(dto.referenceId(), dto.type())) {
      throw new IllegalArgumentException("XP transaction already exists for this reference and type");
    }

    XpTransaction xpTransaction = new XpTransaction();
    xpTransaction.user = user;
    xpTransaction.amount = dto.amount();
    xpTransaction.description = dto.description();
    xpTransaction.referenceId = dto.referenceId();
    xpTransaction.type = dto.type();

    xpTransaction.persist();

    user.xp += dto.amount();
    user.persist();

    return toResponseDTO(xpTransaction);
  }

  @Transactional
  public ResponseXpTransactionDTO update(@NotNull Long id, @Valid UpdateXpTransactionDTO dto) {
    XpTransaction xpTransaction = XpTransaction.findActiveById(id)
        .orElseThrow(() -> new XpTransactionNotFoundException(id));

    int oldAmount = xpTransaction.amount;
    int xpDifference = 0;

    if (dto.amount() != null && dto.amount() != oldAmount) {
      xpDifference = dto.amount() - oldAmount;
      xpTransaction.amount = dto.amount();
    }

    if (dto.description() != null) {
      xpTransaction.description = dto.description();
    }

    xpTransaction.persist();

    if (xpDifference != 0) {
      User user = xpTransaction.user;
      user.xp += xpDifference;
      user.persist();
    }

    return toResponseDTO(xpTransaction);
  }

  @Transactional
  public void delete(@NotNull Long id) {
    XpTransaction xpTransaction = XpTransaction.findActiveById(id)
        .orElseThrow(() -> new XpTransactionNotFoundException(id));

    User user = xpTransaction.user;
    user.xp -= xpTransaction.amount;
    user.persist();

    xpTransaction.softDelete();
    xpTransaction.persist();
  }

  public List<ResponseXpTransactionDTO> findByUserId(@NotNull Long userId) {
    return XpTransaction.findByUserIdActive(userId, 0, Integer.MAX_VALUE)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public List<ResponseXpTransactionDTO> findByDescription(String description, int page, int size) {
    return XpTransaction.findByDescriptionContaining(description, page, size)
        .stream()
        .map(this::toResponseDTO)
        .collect(Collectors.toList());
  }

  public long count() {
    return XpTransaction.countActive();
  }

  private ResponseXpTransactionDTO toResponseDTO(XpTransaction xpTransaction) {
    return new ResponseXpTransactionDTO(
        xpTransaction.id,
        xpTransaction.user.id,
        xpTransaction.user.name,
        xpTransaction.amount,
        xpTransaction.description,
        xpTransaction.referenceId,
        xpTransaction.type,
        xpTransaction.createdAt,
        xpTransaction.updatedAt);
  }
}
