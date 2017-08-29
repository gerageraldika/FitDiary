package com.carpediemsolution.fitdiary;


import com.carpediemsolution.fitdiary.activity.presenters.FitPagerPresenter;
import com.carpediemsolution.fitdiary.activity.views.FitPagerView;
import com.carpediemsolution.fitdiary.dao.FitLab;
import com.carpediemsolution.fitdiary.model.Weight;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by Юлия on 28.08.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class FitPagerPresenterTest {

    private static final String LOG_TAG = "FitPagerPresenterTest";
    private FitPagerView view;
    private FitPagerPresenter presenter;

    private final FitLab fitlab;

    @Before
    public void setUp() throws Exception {

        view = Mockito.mock(FitPagerView.class);
        presenter = new FitPagerPresenter();
    }

    @Test
    public void testCreated() throws Exception {
        assertNotNull(presenter);
    }

    @Test
    public void testNoActionsWithView() throws Exception {
        Mockito.verifyNoMoreInteractions(view);
    }

    //used in production code
    public FitPagerPresenterTest() {
        this(App.getFitLab());
    }

    //used for tests
    FitPagerPresenterTest(FitLab fitlab) {
        this.fitlab = fitlab;
    }

    public List<Weight> getImageURL() {
        return fitlab.getWeights();
    }

    @Test
    public void testBD() throws Exception {
        List<Weight> weightList = new ArrayList<>();
        Weight weight = new Weight();
        weightList.add(weight);

       // App app = Mockito.mock(App.class);
       // FitLab fitlab = Mockito.mock(FitLab.class);
        when(fitlab.getWeights()).thenReturn(weightList);
        FitPagerPresenterTest handler = new FitPagerPresenterTest(fitlab);
        handler.getImageURL();
        Mockito.verify(fitlab, Mockito.atLeastOnce()).getWeights();
        presenter.init();

     //   Mockito.verifyNoMoreInteractions(view);
    }

}
