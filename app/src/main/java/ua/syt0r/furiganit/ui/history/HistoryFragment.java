package ua.syt0r.furiganit.ui.history;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ua.syt0r.furiganit.R;
import ua.syt0r.furiganit.viewmodel.history.HistoryViewModel;

public class HistoryFragment extends Fragment {

    private HistoryViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel.init(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        ProgressBar progressBar = root.findViewById(R.id.progress);

        RecyclerView recyclerView = root.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        HistoryAdapter adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.subscribeOnHistory().observe(this, history -> {
            if (history == null) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
            else if (history.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                adapter.updateData(history);
            }
        });

        return root;
    }

}
