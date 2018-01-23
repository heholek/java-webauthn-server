package demo.webauthn;

import com.yubico.webauthn.data.PublicKeyCredentialDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RegistrationStorage {

    boolean addRegistrationByUsername(String username, CredentialRegistration reg);

    List<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String username);
    Collection<CredentialRegistration> getRegistrationsByUsername(String username);
    Optional<CredentialRegistration> getRegistrationByUsernameAndCredentialId(String username, String idBase64);

    boolean removeRegistrationByUsername(String username, CredentialRegistration credentialRegistration);

    void updateSignatureCountForUsername(String username, String idBase64, long newSignatureCount);

    boolean userHandleOwnsCredential(String userHandleBase64, String idBase64);
    boolean usernameOwnsCredential(String username, String idBase64);

}
