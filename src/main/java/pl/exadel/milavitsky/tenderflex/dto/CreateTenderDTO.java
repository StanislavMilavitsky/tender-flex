package pl.exadel.milavitsky.tenderflex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.exadel.milavitsky.tenderflex.entity.CPVCode;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.TypeOfTender;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateTenderDTO implements Serializable {

    private List<Country> countryList;

    private List<CPVCode> cpvCodes;

    private List<TypeOfTender> typeOfTenders;

    private List<Currency> currencies;
}
