package learn.findingwildlife.security;

import learn.findingwildlife.model.AppUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JwtStringToUserConverter implements Converter<String, AppUser> {
    private final JwtConverter converter;

    public JwtStringToUserConverter(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    public AppUser convert(String s) {
        return converter.getAppUserFromToken(s);
    }
}
