package br.com.multidatasources.api.v1.billionaire;

import br.com.multidatasources.api.ResourceUriHelper;
import br.com.multidatasources.api.v1.billionaire.input.BillionaireInput;
import br.com.multidatasources.api.v1.billionaire.output.BillionaireOutput;
import br.com.multidatasources.config.aop.OTelSpannerCustom;
import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.service.v1.BillionaireService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@OTelSpannerCustom
@RestController
@RequestMapping("/v1/billionaires")
public class BillionaireController {

    private final BillionaireService billionaireService;
    private final BillionairePresenter billionairePresenter;

    public BillionaireController(
        final BillionaireService billionaireService,
        final BillionairePresenter billionairePresenter
    ) {
        this.billionaireService = billionaireService;
        this.billionairePresenter = billionairePresenter;
    }

    @GetMapping
    public ResponseEntity<List<BillionaireOutput>> findAll() {
        List<BillionaireOutput> responseBody = billionairePresenter.present(billionaireService.findAll());
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillionaireOutput> findById(@PathVariable final Long id) {
        BillionaireOutput responseBody = billionairePresenter.present(billionaireService.findById(id));
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<BillionaireOutput> save(@Valid @RequestBody final BillionaireInput input) {
        Billionaire billionaire = billionairePresenter.present(input);

        BillionaireOutput responseBody = billionairePresenter.present(billionaireService.save(billionaire));
        return ResponseEntity.created(ResourceUriHelper.getUri(responseBody.id())).body(responseBody);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillionaireOutput> update(@PathVariable final Long id, @Valid @RequestBody final BillionaireInput input) {
        Billionaire billionaire = billionaireService.findById(id);

        BeanUtils.copyProperties(input, billionaire, "id");

        BillionaireOutput responseBody = billionairePresenter.present(billionaireService.save(billionaire));
        return ResponseEntity.ok(responseBody);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        Billionaire billionaire = billionaireService.findById(id);
        billionaireService.delete(billionaire);
    }

}
