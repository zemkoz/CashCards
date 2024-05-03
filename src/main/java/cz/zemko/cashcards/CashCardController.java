package cz.zemko.cashcards;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/cashcards")
class CashCardController {
    private final CashCardRepository cashCardRepository;

    CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("{id}")
    ResponseEntity<CashCard> findById(@PathVariable Long id, Principal principal) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findByIdAndOwner(id, principal.getName());
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    ResponseEntity<Long> createCashCard(@RequestBody CashCard newCashCardRequest,
                                        UriComponentsBuilder uriComponentsBuilder,
                                        Principal principal) {

        CashCard newCashCard = new CashCard(null,
                newCashCardRequest.amount(),
                principal.getName());
        CashCard savedCashCard = cashCardRepository.save(newCashCard);
        URI locationOfNewCashCard = uriComponentsBuilder
                .path("cashcards/{id}")
                .buildAndExpand(savedCashCard.id())
                .toUri();

        return ResponseEntity
                .created(locationOfNewCashCard)
                .build();
    }

    @GetMapping
    ResponseEntity<Iterable<CashCard>> findAllCashCards(Pageable pageable, Principal principal) {
        var pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
        );
        var cashCards = cashCardRepository.findByOwner(principal.getName(), pageRequest);
        return ResponseEntity.ok(cashCards.getContent());
    }
}
