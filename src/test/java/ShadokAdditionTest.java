import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

@ExtendWith(SoftAssertionsExtension.class)
public class ShadokAdditionTest {

    ShadokAddition additioner = new ShadokAddition();

    @Test
    void nothing_should_return_nothing(SoftAssertions should) {
        validateAddition(should, "GA", "GA", "GA");
    }

    @Test
    void nothing_and_something_should_something(SoftAssertions should) {
        validateAddition(should, "GA", "BU", "BU");
        validateAddition(should, "GA", "ZO", "ZO");
        validateAddition(should, "GA", "MEU", "MEU");
        validateAddition(should, "BU", "GA", "BU");
        validateAddition(should, "ZO", "GA", "ZO");
        validateAddition(should, "MEU", "GA", "MEU");
    }

    @Test
    void should_add_BUs(SoftAssertions should) {
        validateAddition(should, "BU", "BU", "ZO");
    }

    @Test
    void should_add_BU_ZO(SoftAssertions should) {
        validateAddition(should, "BU", "ZO", "MEU");
        validateAddition(should, "ZO", "BU", "MEU");
    }

    @Test
    void should_add_ZO_MEU(SoftAssertions should) {
        validateAddition(should, "MEU", "ZO", "BU BU");
        validateAddition(should, "ZO", "MEU", "BU BU");
    }

    @Test
    void should_add_BU_MEU(SoftAssertions should) {
        validateAddition(should, "MEU", "BU", "BU GA");
        validateAddition(should, "BU", "MEU", "BU GA");
    }

    @Test
    void should_add_BUGA_with_single_word(SoftAssertions should) {
        validateAddition(should, "BU GA", "GA", "BU GA");
        validateAddition(should, "BU GA", "BU", "BU BU");
        validateAddition(should, "BU GA", "ZO", "BU ZO");
        validateAddition(should, "BU GA", "MEU", "BU MEU");
    }

    @Test
    void should_handle_carry(SoftAssertions should) {
        validateAddition(should, "BU BU", "MEU", "ZO GA");
        validateAddition(should, "BU MEU", "BU MEU", "MEU ZO");
        validateAddition(should, "MEU MEU", "ZO ZO", "BU ZO BU");
    }


    private void validateAddition(SoftAssertions should, String left, String right, String expected) {
        System.out.println("ADDING : left = " + left + ", right = " + right);
        String value = additioner.add(left, right);
        should.assertThat(value).as("adding " + left + " and " + right).isEqualTo(expected);
    }
}
