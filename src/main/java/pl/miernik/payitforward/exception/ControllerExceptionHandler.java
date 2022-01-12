package pl.miernik.payitforward.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = { NotExistingRecordException.class })
    public ModelAndView handleConflict(Exception exception) {
        exception.printStackTrace();
        ModelAndView mav = new ModelAndView();
        mav.setStatus(HttpStatus.NOT_FOUND);
        mav.addObject("message", exception.getLocalizedMessage());
        mav.setViewName("/errors/error-message");
        return mav;
    }
}