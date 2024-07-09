package com.example.erp;

import java.util.Hashtable;
import java.util.StringTokenizer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CcAvenueController {

	@PostMapping("/decrypt")
	public Hashtable<String, String> decryptData(@RequestParam("encResp") String encResp) throws Exception {
		String workingKey = "D982922E369694F4EA851296E1AC7817";

		AesCryptUtil aesUtil = new AesCryptUtil(workingKey);
		String decResp = aesUtil.decrypt(encResp);

		StringTokenizer tokenizer = new StringTokenizer(decResp, "&");
		Hashtable<String, String> hs = new Hashtable<>();

		while (tokenizer.hasMoreTokens()) {
			String pair = tokenizer.nextToken();
			if (pair != null) {
				StringTokenizer strTok = new StringTokenizer(pair, "=");
				String pname = "";
				String pvalue = "";
				if (strTok.hasMoreTokens()) {
					pname = strTok.nextToken();
					if (strTok.hasMoreTokens())
						pvalue = strTok.nextToken();
					hs.put(pname, pvalue);
				}
			}
		}

		return hs;
	}

	@PostMapping("/encrypt")
	public String encryptData(@RequestBody String requestData) {
		String workingKey = "D982922E369694F4EA851296E1AC7817";

		try {
			AesCryptUtil aesUtil = new AesCryptUtil(workingKey);
			String encryptedRequest = aesUtil.encrypt(requestData);
			return encryptedRequest;
		} catch (Exception e) {
			return "Error encrypting data: " + e.getMessage();
		}
	}

	@GetMapping("/urls")
	public UrlsResponse getPaymentUrls() {
		String redirectUrl = "http://localhost:8081/MCPG_JSP_KIT_2/ccavResponseHandler.jsp";
		String cancelUrl = "http://localhost:8081/MCPG_JSP_KIT_2/ccavResponseHandler.jsp";
		return new UrlsResponse(redirectUrl, cancelUrl);
	}
}
