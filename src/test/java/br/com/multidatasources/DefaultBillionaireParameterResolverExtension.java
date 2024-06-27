package br.com.multidatasources;

import br.com.multidatasources.model.Billionaire;
import br.com.multidatasources.model.factory.BillionaireBuilder;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;

public class DefaultBillionaireParameterResolverExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(DefaultBillionaire.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return defaultBillionaire(parameterContext.getParameter());
    }

    private static Billionaire defaultBillionaire(final Parameter parameter) {
        if (parameter.getType() != Billionaire.class) {
            return null;
        }

        final var firstName = parameter.getAnnotation(DefaultBillionaire.class).firstName();
        final var lastName = parameter.getAnnotation(DefaultBillionaire.class).lastName();
        final var career = parameter.getAnnotation(DefaultBillionaire.class).career();

        return BillionaireBuilder.builder()
            .firstName(firstName)
            .lastName(lastName)
            .career(career)
            .build();
    }

}
