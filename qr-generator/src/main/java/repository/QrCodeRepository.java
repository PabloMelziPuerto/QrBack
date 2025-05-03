package repository;
import com.qr.qr_generator.model.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

}
