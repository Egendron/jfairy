package io.codearte.jfairy.producer.company;

import com.google.inject.Provider;
import com.google.inject.assistedinject.Assisted;
import io.codearte.jfairy.data.DataMaster;
import io.codearte.jfairy.producer.BaseProducer;
import io.codearte.jfairy.producer.VATIdentificationNumberProvider;
import io.codearte.jfairy.producer.util.TextUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public class CompanyProvider implements Provider<Company> {

	private static final String DOMAIN = "domains";
	private static final String COMPANY_NAME = "companyNames";
	private static final String COMPANY_SUFFIX = "companySuffixes";
	private static final String COMPANY_EMAIL = "companyEmails";

	private String name;
	private String domain;
	private String email;
	private String vatIdentificationNumber;

	private BaseProducer baseProducer;
	private DataMaster dataMaster;

	private final VATIdentificationNumberProvider vatIdentificationNumberProvider;

	@Inject
	public CompanyProvider(BaseProducer baseProducer,
						   DataMaster dataMaster,
						   VATIdentificationNumberProvider vatIdentificationNumberProvider,
						   @Assisted CompanyProperties.CompanyProperty... companyProperties) {
		this.baseProducer = baseProducer;
		this.dataMaster = dataMaster;
		this.vatIdentificationNumberProvider = vatIdentificationNumberProvider;

		for (CompanyProperties.CompanyProperty companyProperty : companyProperties) {
			companyProperty.apply(this);
		}
	}


	@Override
	public Company get() {

		generateName();
		generateDomain();
		generateEmail();
		generateVatIdentificationNumber();

		return new Company(name, domain, email, vatIdentificationNumber);
	}

	private void generateName() {
		if (name != null) {
			return;
		}
		name = dataMaster.getRandomValue(COMPANY_NAME);
		if (baseProducer.trueOrFalse()) {
			name += " " + dataMaster.getRandomValue(COMPANY_SUFFIX);
		}
	}

	private void generateDomain() {
		if (domain != null) {
			return;
		}
//		domain = TextUtils.stripAccents(StringUtils.strip(StringUtils.deleteWhitespace(name.toLowerCase()), ".").replace("/", ""))
//				+ "." + dataMaster.getRandomValue(DOMAIN);
		/**
		 * Modified by @Lhfcws
		 * In case of the illegal hostname characters in company name
		 * And truncate it if it is too long after escape
		 *
		 * It is compatible with other non-latin language and will not change the original result.
		 *
		 * P.S. Actually the best way for Chinese here is to use phonetic writing (so as Japanese or Korean),
		 * but in the original design this CompanyProvider class is not expected to be inherited,
		 * and writing is-else codes for specific language here is not good.
		 */
		String host = TextUtils.stripAccents(StringUtils.strip(StringUtils.deleteWhitespace(name.toLowerCase()), ".").replace("/", ""));
		int len1 = host.length();
		host = StringEscapeUtils.escapeJava(host).replaceAll("\\\\u", "");
		int len2 = host.length();
		if (len2 > len1 && len2 > 10)
			host = host.substring(0, 10);

		domain = host + "." + dataMaster.getRandomValue(DOMAIN);
	}

	private void generateEmail() {
		if (email != null) {
			return;
		}
		email = dataMaster.getRandomValue(COMPANY_EMAIL);
	}

	private void generateVatIdentificationNumber() {
		if (vatIdentificationNumber != null) {
			return;
		}
		vatIdentificationNumber = vatIdentificationNumberProvider.get();
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public void setVatIdentificationNumber(String vatIdentificationNumber) {
		this.vatIdentificationNumber = vatIdentificationNumber;
	}
}
