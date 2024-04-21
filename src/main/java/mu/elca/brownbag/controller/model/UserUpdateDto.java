package mu.elca.brownbag.controller.model;

public record UserUpdateDto(String username,
                            String email,
                            String phoneNumber,
                            String dateOfBirth,
                            String placeOfBirth) {
}
