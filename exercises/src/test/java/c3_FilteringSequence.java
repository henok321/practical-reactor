import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Sequence may produce many elements, but we are not always interested in all of them. In this chapter we will learn
 * how to filter elements from a sequence.
 * <p>
 * Read first:
 * <p>
 * https://projectreactor.io/docs/core/release/reference/#which.filtering
 * <p>
 * Useful documentation:
 * <p>
 * https://projectreactor.io/docs/core/release/reference/#which-operator
 * https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Mono.html
 * https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html
 *
 * @author Stefan Dragisic
 */
class c3_FilteringSequence extends FilteringSequenceBase {

    /**
     * Collect most popular girl names, no longer then 4 characters.
     */
    @Test
    void girls_are_made_of_sugar_and_spice() {
        final Flux<String> shortListed = popular_girl_names_service().filter(name -> name.length() < 5);

        StepVerifier.create(shortListed)
                .expectNext("Emma", "Ava", "Mia", "Luna", "Ella")
                .verifyComplete();
    }

    /**
     * `mashed_data_service()` returns sequence of generic objects.
     * Without using `filter()` operator, collect only objects that are instance of `String`
     */
    @Test
    void needle_in_a_haystack() {
        final Flux<String> strings = mashed_data_service().ofType(String.class);

        StepVerifier.create(strings)
                .expectNext("1", "String.class")
                .verifyComplete();
    }

    /**
     * This service may return duplicated data. Filter out all the duplicates from the sequence.
     */
    @Test
    void economical() {
        final Flux<String> items = duplicated_records_service().distinct();

        StepVerifier.create(items)
                .expectNext("1", "2", "3", "4", "5")
                .verifyComplete();
    }

    /**
     * This service returns many elements, but you are only interested in the first one.
     * Also, service is very fragile, if you pull more than needed, you may brake it.
     * <p>
     * This time no blocking. Use only one operator.
     */
    @Test
    void watch_out_for_the_spiders() {

        final Mono<String> firstResult = fragile_service().next();

        StepVerifier.create(firstResult)
                .expectNext("watch_out")
                .verifyComplete();
    }

    /**
     * `number_service()` returns 300 numbers, but you only need first 100 numbers.
     */
    @Test
    void dont_take_more_then_you_need() {
        final Flux<Integer> numbers = number_service().take(100);

        StepVerifier.create(numbers)
                .expectNextCount(100)
                .verifyComplete();
    }

    /**
     * `number_service()` returns 300 numbers, but you only need last 100 numbers.
     */
    @Test
    void not_a_binary_search() {
        final Flux<Integer> numbers = number_service().skip(200);

        StepVerifier.create(numbers)
                .expectNextMatches(i -> i >= 200)
                .expectNextCount(99)
                .verifyComplete();
    }

    /**
     * `number_service()` returns 300 numbers, but you only need 100 numbers, from the middle.
     */
    @Test
    void golden_middle() {
        final Flux<Integer> numbers = number_service().skip(100).take(100);
        StepVerifier.create(numbers)
                .expectNextMatches(i -> i >= 100)
                .expectNextCount(99)
                .verifyComplete();
    }
}
