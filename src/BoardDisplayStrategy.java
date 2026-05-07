import java.awt.*;

public interface BoardDisplayStrategy {
    void paintPit(Graphics2D g2, PitButton pit);
    void paintStore(Graphics2D g2, MancalaStoreComponent store);
    void configurePitButton(PitButton pit);
}
