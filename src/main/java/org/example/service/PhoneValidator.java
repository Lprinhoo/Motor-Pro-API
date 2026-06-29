package org.example.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.example.exception.InvalidContatoException;
import org.springframework.stereotype.Component;

/**
 * Valida e normaliza números de telefone/WhatsApp usando a base de
 * metadados de operadoras e planos de numeração da libphonenumber
 * (mesma lib usada pelo Android e por apps como WhatsApp).
 *
 * Isso garante que o número:
 * - tem uma quantidade de dígitos compatível com o país;
 * - tem um DDD/prefixo que realmente existe;
 * - segue um padrão de numeração válido (fixo ou móvel).
 *
 * Importante: isso NÃO garante que a linha está ativa/em uso hoje.
 * Para essa confirmação "está mesmo em uso" é necessário um passo
 * adicional de verificação por SMS/WhatsApp (código OTP).
 */
@Component
public class PhoneValidator {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    private static final String REGIAO_PADRAO = "BR";

    /**
     * Valida o número e retorna a versão normalizada em formato E.164
     * (ex: "+5585999999999"), que é o formato que deve ser persistido
     * no banco para evitar duplicidade de números com formatações
     * diferentes ("(85) 99999-9999", "85999999999", etc).
     *
     * @throws InvalidContatoException se o número não for válido
     */
    public String validarENormalizar(String valorOriginal) {
        if (valorOriginal == null || valorOriginal.isBlank()) {
            throw new InvalidContatoException("O número de telefone não pode estar em branco.");
        }

        String valor = valorOriginal.trim();

        try {
            PhoneNumber numero = phoneUtil.parse(valor, REGIAO_PADRAO);

            if (!phoneUtil.isValidNumber(numero)) {
                throw new InvalidContatoException(
                        "O número de telefone informado não é válido. Verifique o DDD e a quantidade de dígitos.");
            }

            return phoneUtil.format(numero, PhoneNumberUtil.PhoneNumberFormat.E164);

        } catch (NumberParseException e) {
            throw new InvalidContatoException(
                    "Não foi possível interpretar o número de telefone informado. Use um formato como (85) 99999-9999.");
        }
    }
}
