package co.edu.umanizales.ecobin_csv_api.controller;

import co.edu.umanizales.ecobin_csv_api.model.Redemption;
import co.edu.umanizales.ecobin_csv_api.service.RedemptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redemptions")
public class RedemptionController {

    private final RedemptionService service;

    public RedemptionController(RedemptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Redemption> list() {
        return service.list();
    }

    @PostMapping("/{citizenId}/{rewardId}")
    public ResponseEntity<?> redeem(
            @PathVariable long citizenId,
            @PathVariable long rewardId) {
        try {
            Redemption r = service.create(citizenId, rewardId);
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}