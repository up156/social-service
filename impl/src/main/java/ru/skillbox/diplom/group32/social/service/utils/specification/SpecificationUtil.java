package ru.skillbox.diplom.group32.social.service.utils.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.group32.social.service.model.base.BaseEntity_;
import ru.skillbox.diplom.group32.social.service.model.base.BaseSearchDto;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.function.Supplier;

@Component
public class SpecificationUtil {

    public static final Specification EMPTY_SPECIFICATION = (root, query, criteriaBuilder) -> {return null;};

    public static <T, V> Specification<T> in(SingularAttribute<T, V> field, Collection<V> value, boolean isSkipNullValues) {
        return nullValueCheck(value, isSkipNullValues, () -> {
            return (root, query, builder) -> {
                query.distinct(true);
                return root.get(field).in(value);
            };
        });
    }

    public static <T, V> Specification<T> equal(SingularAttribute<T, V> field, V value, boolean isSkipNullValues){
        return nullValueCheck(value, isSkipNullValues, () -> {
            return ((root, query, builder) -> {
                query.distinct(true);
                return builder.equal(root.get(field), value);
            });
        });
    }
    public static <T> Specification<T> like(SingularAttribute<T, String> field, String value, boolean isSkipNullValues){
        return nullValueCheck(value, isSkipNullValues, () -> {
            return ((root, query, builder) -> {
                query.distinct(true);
                return builder.like(root.get(field), "%" + value + "%");
            });
        });
    }
    public static Specification getBaseSpecification(BaseSearchDto searchDto) {
        return equal(BaseEntity_.id, searchDto.getId(), true)
                .and(equal(BaseEntity_.isDeleted, searchDto.getIsDeleted(), true));
    }
    private static <T, V> Specification<T> nullValueCheck(V value, boolean isSkipNullValues, Supplier<Specification<T>> specificationSupplier) {
        return value == null && isSkipNullValues ? EMPTY_SPECIFICATION : (Specification) specificationSupplier.get();
    }
}