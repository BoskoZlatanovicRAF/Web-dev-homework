package service;

import model.Quote;

import java.util.ArrayList;
import java.util.List;

public class QuotesService {
    private final List<Quote> quotes = new ArrayList<>();

    public void saveQuote(Quote quote) {
        quotes.add(quote);
    }

    public List<Quote> getQuotes() {
        return quotes;
    }
}
