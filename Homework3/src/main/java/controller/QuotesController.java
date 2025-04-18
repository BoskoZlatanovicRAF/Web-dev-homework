package controller;

import glavniServis.ServerThread;
import http.request.Request;
import http.response.HtmlResponse;
import http.response.RedirectResponse;
import http.response.Response;
import model.Quote;
import service.QuotesService;

public class QuotesController extends Controller{

    private final QuotesService quotesService;

    public QuotesController(Request request, QuotesService quotesService) {
        super(request);
        this.quotesService = quotesService;
    }
    @Override
    public Response doGet() {
        StringBuilder html = new StringBuilder("<html><body>");
        html.append("<form method = \"POST\" action = \"/save-quote\">");
        html.append("<label>Quote: </label>");
        html.append("<input name = \"quote\" type = \"text\"><br><br>");
        html.append("<label>Author: </label>");
        html.append("<input name = \"author\" type = \"text\"><br><br>");
        html.append("<button>Save Quote</button>");
        html.append("</form>");

        for(Quote quote : quotesService.getQuotes()){
            html.append("<p>");
            html.append("<b>");
            html.append(quote.getAuthor()).append(": ");
            html.append("</b>");
            html.append(quote.getQuote());
            html.append("</p>");
        }

        try{
            Quote quote = ServerThread.getQuoteOfTheDay();
            html.append("<h2>Quote of the day</h2>");
            html.append("<p>").append(quote.getAuthor()).append(": ").append(quote.getQuote()).append("</p>");
        }catch (Exception e){
            html.append("<p>Error fetching the quote of the day.</p>");
        }

        html.append("</body></html>");

        return new HtmlResponse(html.toString());
    }

    @Override
    public Response doPost() {
        if(!request.getBody().contains("=")){
            return new RedirectResponse("/quotes");
        }

        //na primer quote=Ovo+je+moj+citat&author=Ja
        String[] parts = request.getBody().split("&");
        String quoteText = parts[0].split("=")[1];
        quoteText = quoteText.replace("+", " ");
        String author = parts[1].split("=")[1];

        Quote quote = new Quote(author, quoteText);
        quotesService.saveQuote(quote);

        return new RedirectResponse("/quotes");
    }
}
