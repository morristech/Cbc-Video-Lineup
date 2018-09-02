package ca.nick.rxcbcmpx.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import ca.nick.rxcbcmpx.networking.AggregateApiService;
import ca.nick.rxcbcmpx.networking.PolopolyService;
import ca.nick.rxcbcmpx.networking.ThePlatformService;
import ca.nick.rxcbcmpx.networking.TpFeedService;

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

    private VideoRepository videoRepository;

    @Before
    public void setUp() {
        when(cbcDatabase.videoDao()).thenReturn(videoDao);
        videoRepository = new VideoRepository(cbcDatabase, aggregateApiService, polopolyService,
                tpFeedService, thePlatformService);
    }

    @Test
    public void nukeDatabase() {
        videoRepository.nukeDatabase()
                .test();

        verify(videoDao).nuke();
    }
}