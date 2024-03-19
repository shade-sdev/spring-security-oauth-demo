package mu.elca.brownbag.controller.model;

public record UserUpdateDto(String firstName,
                            String lastName,
                            String email,
                            String phoneNumber,
                            String dateOfBirth,
                            String placeOfBirth) {
}
