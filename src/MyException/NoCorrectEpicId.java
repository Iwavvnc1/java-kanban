package MyException;


    public class NoCorrectEpicId extends RuntimeException {
        public NoCorrectEpicId(final String message) {
            super(message);
        }
    }

