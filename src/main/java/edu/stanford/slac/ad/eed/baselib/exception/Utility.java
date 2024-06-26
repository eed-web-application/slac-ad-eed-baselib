package edu.stanford.slac.ad.eed.baselib.exception;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Utility {
    /**
     * Wrap a method within a try/catch and generate, in case of
     * exception, a throw for @ControllerLogicException using input parameter
     *
     * @param callable     callable to execute
     * @param errorCode    the error code in case of exception
     * @param errorMessage the message in case of exception
     * @param errorDomain  the domain for the exception
     * @param <T>          The generic type
     * @return the result of callable
     */
    static public <T> T wrapCatch(
            Callable<T> callable,
            int errorCode,
            String errorMessage,
            String errorDomain) {
        try {
            return callable.call();
        } catch (ControllerLogicException e) {
            throw e;
        } catch (Exception e) {
            throw ControllerLogicException.builder()
                    .errorCode(errorCode)
                    .errorMessage(errorMessage)
                    .errorDomain(errorDomain)
                    .build(); // or return null, or whatever you want
        }
    }

    /**
     * Convert exception in @ControllerLogicException one
     *
     * @param callable    the callable that can throw an exception
     * @param errorCode   the error
     * @param errorDomain the domain
     * @param <T>         the type
     * @return the result of the callable
     */
    static public <T> T wrapCatch(
            Callable<T> callable,
            int errorCode,
            String errorDomain) {
        try {
            return callable.call();
        } catch (ControllerLogicException e) {
            throw e;
        } catch (Exception e) {
            var ex = ControllerLogicException.builder()
                    .errorCode(errorCode)
                    .errorMessage(e.getMessage())
                    .errorDomain(errorDomain)
                    .build();
            ex.setStackTrace(e.getStackTrace());
            throw ex; // or return null, or whatever you want
        }
    }

    /**
     * Convert exception in @ControllerLogicException one
     * @param callable the callable that can throw an exception
     * @param errorCode the error
     * @return the result of the callable
     * @param <T> the type
     */
    static public <T> T wrapCatch(
            Callable<T> callable,
            int errorCode) {
        try {
            return callable.call();
        } catch (ControllerLogicException e) {
            throw e;
        } catch (Exception e) {
            throw ControllerLogicException.builder()
                    .errorCode(errorCode)
                    .errorMessage(e.getMessage())
                    .errorDomain(getAllMethodInCall())
                    .build(); // or return null, or whatever you want
        }
    }

    /**
     * Wrap a method within a try/catch and generate, in case of
     * @param callable  the callable that can throw an exception
     * @param errorCode the error
     * @param errorMessage the message
     * @return the result of the callable
     * @param <T> the type
     */
    static public <T> T wrapCatchWMsg(
            Callable<T> callable,
            int errorCode,
            String errorMessage) {
        try {
            return callable.call();
        } catch (ControllerLogicException e) {
            throw e;
        } catch (Exception e) {
            throw ControllerLogicException.builder()
                    .errorCode(errorCode)
                    .errorMessage(errorMessage)
                    .errorDomain(getAllMethodInCall())
                    .build(); // or return null, or whatever you want
        }
    }

    /**
     * Throw @ControllerLogicException on assertion fails
     *
     * @param callable     the callable that return a boolean
     * @param errorCode    the error
     * @param errorMessage the error
     * @param errorDomain  the domain
     */
    static public void assertion(Callable<Boolean> callable, int errorCode, String errorMessage, String errorDomain) {
        try {
            if (!callable.call())
                throw ControllerLogicException.builder()
                        .errorCode(errorCode)
                        .errorMessage(errorMessage)
                        .errorDomain(errorDomain)
                        .build(); // or return null, or whatever you want
        } catch (ControllerLogicException e) {
            throw e;
        } catch (Exception e) {
            throw ControllerLogicException.builder()
                    .errorCode(errorCode)
                    .errorMessage(errorMessage)
                    .errorDomain(errorDomain)
                    .build(); // or return null, or whatever you want
        }
    }

    static public void assertion(Callable<Boolean> callable, ControllerLogicException exception) {
        try {
            if (!callable.call())
                throw exception;
        } catch (ControllerLogicException e) {
            throw e;
        } catch (Exception e) {
            throw ControllerLogicException.builder()
                    .errorCode(exception.getErrorCode())
                    .errorMessage(e.getMessage())
                    .errorDomain(exception.getErrorDomain())
                    .build(); // or return null, or whatever you want
        }
    }

    /**
     * All the check needs to be true
     *
     * @param exception the exception to fire
     * @param checks    all the checks
     */
    @SafeVarargs
    static public void assertion(
            ControllerLogicException exception,
            Supplier<Boolean>... checks
    ) {
        String callerClass = Thread.currentThread().getStackTrace()[2].getClassName();
        String callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();

        if (!Arrays.stream(checks).allMatch(Supplier::get)) {
            throw exception;
        }
    }

    /**
     * Return exception in case all the check return false
     *
     * @param checks    the list of the check to do
     * @param exception the exception to throw
     */
    static public void assertionAllFalse(
            List<Supplier<Boolean>> checks,
            ControllerLogicException exception
    ) {
        String callerClass = Thread.currentThread().getStackTrace()[2].getClassName();
        String callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (checks.stream().noneMatch(Supplier::get)) {
            throw exception;
        }
    }

    /**
     * Return exception in case all the check return true
     *
     * @param checks    the list of the check to do
     * @param exception the exception to throw
     */
    static public void assertionAllTrue(
            List<Supplier<Boolean>> checks,
            ControllerLogicException exception
    ) {
        String callerClass = Thread.currentThread().getStackTrace()[2].getClassName();
        String callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (!checks.stream().allMatch(Supplier::get)) {
            throw exception;
        }
    }

    /**
     * Check at least any check is true
     *
     * @param checks    the list of the check to do
     * @param exception the exception to throw
     */
    static public void assertionAnyTrue(
            List<Supplier<Boolean>> checks,
            ControllerLogicException exception
    ) {
        String callerClass = Thread.currentThread().getStackTrace()[2].getClassName();
        String callerMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        if (checks.stream().noneMatch(Supplier::get)) {
            throw exception;
        }
    }

    /**
     * Perform an any match operation on the supplier
     *
     * @param checks all the checks
     * @return true if any of the check return true
     */
    @SafeVarargs
    static public boolean any(Supplier<Boolean>... checks) {
        return Arrays.stream(checks).anyMatch(Supplier::get);
    }
    /**
     * Perform an any match operation on the supplier
     *
     * @param checks all the checks
     * @return true if any of the check return true
     */
    static public boolean any(List<Supplier<Boolean>> checks) {
        return checks.stream().anyMatch(Supplier::get);
    }

    /**
     * Perform an any operation on the supplier
     *
     * @param checks all the checks
     * @return true if all the check return true
     */
    @SafeVarargs
    static public boolean all(Supplier<Boolean>... checks) {
        return Arrays.stream(checks).allMatch(Supplier::get);
    }

    /**
     * Perform an any operation on the supplier
     *
     * @param checks all the checks
     * @return true if all the check return true
     */
    static public boolean all(List<Supplier<Boolean>> checks) {
        return checks.stream().allMatch(Supplier::get);
    }
    /**
     * Perform an any operation on the supplier
     *
     * @param checks all the checks
     * @return true if none of the check return true
     */
    @SafeVarargs
    static public boolean none(Supplier<Boolean>... checks) {
        return Arrays.stream(checks).noneMatch(Supplier::get);
    }
    /**
     * Perform an any operation on the supplier
     *
     * @param checks all the checks
     * @return true if none of the check return true
     */
    static public boolean none(List<Supplier<Boolean>> checks) {
        return checks.stream().noneMatch(Supplier::get);
    }

    /**
     * Get all the method in call, starting from edu.stanford.slac.ad.eed.baselib.exception.Utility.getAllMethodInCall()
     * @return the method in call
     */
    static public String getAllMethodInCall() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int idx = 3; idx < stackTraceElements.length; idx++) {
            if(stackTraceElements[idx].getClassName().contains("edu.stanford.slac.") &&
                    !stackTraceElements[idx].getClassName().contains("<") &&
                    !stackTraceElements[idx].getClassName().contains("$") &&
                    !stackTraceElements[idx].getMethodName().contains("<") &&
                    !stackTraceElements[idx].getMethodName().contains("$")
            ) {
                sb.append(stackTraceElements[idx].getClassName());
                sb.append("::");
                sb.append(stackTraceElements[idx].getMethodName());
                break;
            }
        }
        return sb.toString();
    }
}
