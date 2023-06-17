package br.com.multidatasources.api.v1.billionaire;

import br.com.multidatasources.api.v1.billionaire.input.BillionaireInput;
import br.com.multidatasources.api.v1.billionaire.output.BillionaireOutput;
import br.com.multidatasources.model.Billionaire;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillionairePresenter {

    Billionaire present(final BillionaireInput inputDto);
    BillionaireOutput present(final Billionaire model);
    List<BillionaireOutput> present(final List<Billionaire> modelList);

}
