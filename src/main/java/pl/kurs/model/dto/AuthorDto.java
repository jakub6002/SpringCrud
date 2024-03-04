package pl.kurs.model.dto;

public record AuthorDto(int id, String firstName, String lastName, Integer birthDate, Integer deathDate) {

    public static AuthorDto from(pl.kurs.model.Author author) {
        return new AuthorDto(author.getId(), author.getName(), author.getSurname(), author.getBirthYear(), author.getDeathYear());
    }

}
