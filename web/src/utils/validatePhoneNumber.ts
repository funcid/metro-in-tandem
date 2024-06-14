/**
 * Validates a Russian phone number.
 * @param {string} phoneNumber - The phone number to validate.
 * @returns {boolean} - Returns true if the phone number is valid, otherwise false.
 */
function validateRussianPhoneNumber(phoneNumber: string) {
    // Regular expression for validating Russian phone numbers
    const regex = /^(?:\+7|8)[-\s]?\(?\d{3}\)?[-\s]?\d{3}[-\s]?\d{2}[-\s]?\d{2}$/;

    return regex.test(phoneNumber);
}

export default validateRussianPhoneNumber;