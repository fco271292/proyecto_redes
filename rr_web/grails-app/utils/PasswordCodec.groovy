import java.security.MessageDigest
import org.apache.commons.codec.binary.Base64

//https://grails.org/wiki/Simple%20Dynamic%20Password%20Codec

class PasswordCodec {

	static encode = { String s ->
		MessageDigest md = MessageDigest.getInstance('SHA')
		md.update s.getBytes('UTF-8')
		Base64.encodeBase64 md.digest()
	}
}