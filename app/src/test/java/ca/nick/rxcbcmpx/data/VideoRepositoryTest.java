package ca.nick.rxcbcmpx.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import ca.nick.rxcbcmpx.models.AggregateItem;
import ca.nick.rxcbcmpx.models.PolopolyItem;
import ca.nick.rxcbcmpx.models.ThePlatformItem;
import ca.nick.rxcbcmpx.models.TpFeedItem;
import ca.nick.rxcbcmpx.networking.AggregateApiService;
import ca.nick.rxcbcmpx.networking.PolopolyService;
import ca.nick.rxcbcmpx.networking.ThePlatformService;
import ca.nick.rxcbcmpx.networking.TpFeedService;
import io.reactivex.Flowable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VideoRepositoryTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private CbcDatabase cbcDatabase;
    @Mock
    private VideoDao videoDao;
    @Mock
    private AggregateApiService aggregateApiService;
    @Mock
    private PolopolyService polopolyService;
    @Mock
    private ThePlatformService thePlatformService;
    @Mock
    private TpFeedService tpFeedService;
    @Mock
    private AggregateItem aggregateItem;
    @Mock
    private PolopolyItem polopolyItem;
    @Mock
    private ThePlatformItem thePlatformItem;
    @Mock
    private TpFeedItem tpFeedItem;

    private List<AggregateItem> aggregateItems = new ArrayList<>();
    private VideoRepository videoRepository;

    private static final String FAKE_SOURCE_ID = "1.2345678";

    @Before
    public void setUp() {
        aggregateItems.add(aggregateItem);
        when(aggregateItem.getSourceId()).thenReturn(FAKE_SOURCE_ID);
        when(cbcDatabase.videoDao()).thenReturn(videoDao);
        when(aggregateApiService.topStoriesVideos()).thenReturn(Flowable.just(aggregateItems));
        when(polopolyService.story(anyString())).thenReturn(Flowable.just(polopolyItem));
        when(tpFeedService.byGuid(anyString())).thenReturn(Flowable.just(tpFeedItem));
        when(thePlatformService.thePlatformItem(anyString())).thenReturn(Flowable.just(thePlatformItem));

        videoRepository = new VideoRepository(cbcDatabase, aggregateApiService, polopolyService,
                tpFeedService, thePlatformService);
    }

    @Test
    public void nukeDatabase() {
        videoRepository.nukeDatabase()
                .test();

        verify(videoDao).nuke();
    }

    @Test
    public void fetchVideoContent_mustHitPolopoly() {
        when(aggregateItem.isPolopolySource()).thenReturn(true);

        videoRepository.fetchVideoContent()
                .test();

        verify(polopolyService).story(FAKE_SOURCE_ID);
    }

    @Test
    public void fetchVideoContent_skipsPolopoly() {
        when(aggregateItem.isPolopolySource()).thenReturn(false);

        videoRepository.fetchVideoContent()
                .test();

        verify(polopolyService, times(0)).story(FAKE_SOURCE_ID);
    }
}