package cz.zemko.cashcards;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashcards")
class CashCardController {
    @GetMapping("{id}")
    ResponseEntity<CashCard> findById(@PathVariable Long id) {
        if (id != 99L) {
            return ResponseEntity.notFound().build();
        }
        CashCard cashCard = new CashCard(99L, 123.45D);
        return ResponseEntity.ok(cashCard);
    }
}
