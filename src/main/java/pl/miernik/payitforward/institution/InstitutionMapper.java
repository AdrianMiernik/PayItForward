package pl.miernik.payitforward.institution;

import org.springframework.stereotype.Component;

@Component
public class InstitutionMapper {

    public InstitutionDto institutionToDto(Institution institution) {
        return InstitutionDto.builder()
                .id(institution.getId())
                .name(institution.getName())
                .description(institution.getDescription())
                .city(institution.getCity()).build();
    }

    public Institution dtoToInstitution(InstitutionDto institutionDto) {
        return Institution.builder()
                .id(institutionDto.getId())
                .name(institutionDto.getName())
                .description(institutionDto.getDescription())
                .city(institutionDto.getCity()).build();
    }
}