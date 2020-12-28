package external.logitech.harmony.protocol;

import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class EmptyIncrementedIdReplyFilter implements StanzaFilter {

    private final AndFilter filter;

    public EmptyIncrementedIdReplyFilter(OAStanza request, XMPPTCPConnection connection) {
        StanzaFilter iqFilter = new OrFilter(IQTypeFilter.ERROR, IQTypeFilter.RESULT);
        StanzaFilter idFilter = new StanzaIdFilter(incrementStanzaId(request));
        filter = new AndFilter(iqFilter, idFilter);
    }

    @Override
    public boolean accept(Stanza stanza) {
        return filter.accept(stanza);
    }

    private Pattern numericIdRE = Pattern.compile("^(.*?)(\\d+)$");

    private String incrementStanzaId(OAStanza request) {
        String stanzaId = request.getStanzaId();
        Matcher matcher = numericIdRE.matcher(stanzaId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(format("Can't handle non-numeric stanza id %s", stanzaId));
        }
        String beginning = matcher.group(1);
        Long stanzaNum = Long.parseLong(matcher.group(2));
        return format("%s%d", beginning, (stanzaNum + 1));
    }
}
