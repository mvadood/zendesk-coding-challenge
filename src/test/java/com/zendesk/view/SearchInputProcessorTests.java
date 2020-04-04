package com.zendesk.view;

import static junit.framework.TestCase.assertEquals;

import com.zendesk.exception.input.NotSupportedEntityTypeException;
import com.zendesk.exception.input.NotSupportedFieldException;
import com.zendesk.model.entity.User;
import com.zendesk.model.request.UserField;
import java.lang.reflect.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SearchInputProcessor.class})
public class SearchInputProcessorTests {


  @Autowired
  SearchInputProcessor searchInputProcessor;

  @Test(expected = NotSupportedEntityTypeException.class)
  public void getSearchEntityShouldThrowException() throws NotSupportedEntityTypeException {
    searchInputProcessor.getSearchEntity("imaginary_entity");
  }

  @Test(expected = NotSupportedFieldException.class)
  public void checkFiledsShouldThrowException() throws NotSupportedFieldException {
    searchInputProcessor.checkIfFieldExists(User.class, "imaginary_field");
  }

  @Test
  public void searchForEntityShouldReturnUser() throws NotSupportedEntityTypeException {
    Type type = searchInputProcessor.getSearchEntity("user");
    assertEquals(User.class, type);
  }

  @Test
  public void convertDatetimeShouldWorkWithTimestampsAndStrings() {

    long ts = System.currentTimeMillis();
    assertEquals(ts / 1000, searchInputProcessor.convertTo(String.valueOf(ts / 1000),
        UserField.getFieldType(UserField.CREATED_AT)));

    searchInputProcessor
        .convertTo("2016-04-15T05:19:46 -10:00", UserField.getFieldType(UserField.CREATED_AT));
  }

  @Test
  public void fieldKeyTranslationShouldReturnCamelCase() {
    assertEquals("createdAt", searchInputProcessor.convertFieldKeyString("created_at"));
  }

}
