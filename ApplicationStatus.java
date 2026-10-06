import java.io.Serializable;

public enum ApplicationStatus implements Serializable
{
    PENDING,
    IN_REVIEW,
    SELECTED,
    REJECTED
}