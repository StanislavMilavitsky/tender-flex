package pl.exadel.milavitsky.tenderflex.mapper;

import org.springframework.stereotype.Component;
import pl.exadel.milavitsky.tenderflex.dto.OfferDto;
import pl.exadel.milavitsky.tenderflex.entity.Offer;
import pl.exadel.milavitsky.tenderflex.entity.enums.Country;
import pl.exadel.milavitsky.tenderflex.entity.enums.Currency;
import pl.exadel.milavitsky.tenderflex.entity.enums.StatusOffer;

import java.time.LocalDate;

@Component
public class OfferMapper implements Mapper<OfferDto, Offer> {

    @Override
    public OfferDto toDTO(Offer offer) {
        OfferDto offerDto = new OfferDto();
        offerDto.setOfficialName(offer.getOfficialName());
        offerDto.setNationalRegistrationNumber(offer.getNationalRegistrationNumber());
        offerDto.setCountry(offer.getCountry().name());
        offerDto.setCity(offer.getCity());
        offerDto.setName(offer.getName());
        offerDto.setSurname(offer.getSurname());
        offerDto.setPhoneNumber(offer.getPhoneNumber());
        offerDto.setBidPrice(offer.getBidPrice());
        offerDto.setCurrency(offer.getCurrency().name());
        offerDto.setDocument(offer.getDocument());
        offerDto.setStatus(offer.getStatus().name());
        offerDto.setSentDate(offer.getSentDate().toString());
        offerDto.setId(offer.getId());
        return offerDto;
    }

    @Override
    public Offer fromDTO(OfferDto offerDto) {
        Offer offer = new Offer();
        offer.setOfficialName(offerDto.getOfficialName());
        offer.setNationalRegistrationNumber(offerDto.getNationalRegistrationNumber());
        offer.setCountry(Country.valueOf(offerDto.getCountry()));
        offer.setCity(offerDto.getCity());
        offer.setName(offerDto.getName());
        offer.setSurname(offerDto.getSurname());
        offer.setPhoneNumber(offerDto.getPhoneNumber());
        offer.setBidPrice(offerDto.getBidPrice());
        offer.setCurrency(Currency.valueOf(offerDto.getCurrency()));
        offer.setDocument(offerDto.getDocument());
        offer.setStatus(StatusOffer.valueOf(offerDto.getStatus()));
        offer.setSentDate(LocalDate.parse(offerDto.getSentDate()));
        offer.setId((offerDto.getId()));
        return offer;
    }
}
