package Blind.Sight.community.exception;

public class ApiErrorMessages {
    // Bad request (400):
    public static final String INVALID_REQUEST_PARAMS = "Invalid request parameters.";
    public static final String MISSING_REQUIRED_FIELDS = "Missing required fields.";
    public static final String INVALID_JSON_PAYLOAD = "Invalid JSON payload.";
    public static final String INVALID_INPUT_FORMAT = "Invalid input format.";

    // Unauthorized (401):
    public static final String AUTHENTICATION_FAILED = "Authentication failed.";
    public static final String INVALID_OR_EXPIRED_TOKEN = "Invalid or expired token.";
    public static final String INSUFFICIENT_PRIVILEGES = "Insufficient privileges.";
    public static final String ACCESS_DENIED = "Access denied.";

    // Forbidden (403):
    public static final String ACCESS_FORBIDDEN = "Access forbidden.";
    public static final String INSUFFICIENT_PERMISSION = "Insufficient permissions.";
    public static final String RESOURCE_ACCESS_FORBIDDEN = "Resource access is forbidden.";

    // Not Found (404):
    public static final String RESOURCE_NOT_FOUND = "Resource not found.";
    public static final String ENDPOINT_NOT_FOUND = "Endpoint not found.";
    public static final String INVALID_URL = "Invalid URL.";

    // Method Not Allowed (405):
    public static final String METHOD_NOT_SUPPORTED = "HTTP method not supported for this endpoint.";
    public static final String INVALID_REQUEST_METHOD = "Invalid request method.";

    // Conflict (409):
    public static final String RESOURCE_ALREADY_EXISTS = "Resource already exists.";
    public static final String DATA_CONFLICT = "Data conflict detected.";
    public static final String DUPLICATE_RESOURCE = "Duplicate resource.";

    // Unsupported Media Type (415):
    public static final String UNSUPPORTED_CONTENT_TYPE = "Unsupported content type.";
    public static final String INVALID_MEDIA_TYPE = "Invalid media type in the request header.";

    // Unprocessable Entity (422):
    public static final String VALIDATION_ERROR = "Validation error.";
    public static final String INVALID_DATA_SUBMITTED = "Invalid data submitted.";
    public static final String DATA_INTEGRITY_VIOLATION = "Data integrity violation.";

    // Internal Server Error (500):
    public static final String INTERNAL_SERVER_ERROR = "Internal server error.";
    public static final String UNEXPECTED_SERVER_ERROR = "Unexpected server error.";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong on our end.";

    // Service Unavailable (503):
    public static final String SERVICE_UNAVAILABLE = "Service temporarily unavailable.";
    public static final String SERVER_DOWN_FOR_MAINTENANCE = "Server is down for maintenance.";
}
