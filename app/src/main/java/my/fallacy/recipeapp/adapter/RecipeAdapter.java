package my.fallacy.recipeapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.fallacy.recipeapp.R;
import my.fallacy.recipeapp.model.Recipe;

/**
 * Created by amirf on 20/08/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private OnItemClickListener mOnItemClickListener;
    private Context context;

    public RecipeAdapter(Context context, final List<Recipe> x,
                         final OnItemClickListener onItemClickListener) {
        this.context = context;
        recipeList = x;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Recipe recipe = recipeList.get(position);

        holder.tRecipeName.setText(recipe.getName());
        holder.tRecipeType.setText(recipe.getType());

        holder.mCardRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(recipe.getId());
                }
            }
        });
        holder.iRecipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemDelete(recipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_recipe) CardView mCardRecipe;
        @BindView(R.id.t_recipe_name) TextView tRecipeName;
        @BindView(R.id.t_recipe_type) TextView tRecipeType;
        @BindView(R.id.i_recipe_delete) ImageView iRecipeDelete;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(Integer recipeID);
        void onItemDelete(Recipe recipe);
    }

}
