import { useEffect, useState } from "react";
import Proptypes from "prop-types";
import fetchGet from "../../utility/fetchGet";

function ProductCategoryForm({onSelectCategoryIdChange, selectedCategoryId, selectedSubcategoryId, onSelectedSubCategoryIdChange}){
    const [avaibleCategories, setAvaibleCategories] = useState();
    const [avaibleSubCategories, setAvaibleSubCategories] = useState();

    useEffect(() => {
        const getCategories = async () => {
            const categoryData = await fetchGet('/api/product/category');
            const categoryResponse = await categoryData.json();

            setAvaibleCategories(categoryResponse);
        }

        getCategories();
    }, []);

    useEffect(() => {
        if(!selectedCategoryId){
            setAvaibleSubCategories('');
            onSelectedSubCategoryIdChange('');
        }

        const getSubCategories = async () => {
            const subCategoryData = await fetchGet(`/api/product/subcategory/${selectedCategoryId}`)
            const subCategoryResponse = await subCategoryData.json();

            setAvaibleSubCategories(subCategoryResponse);
        }

        if(selectedCategoryId){
            getSubCategories();
        }
    }, [selectedCategoryId]);

    return(
        <>
        {avaibleCategories && 
                <div className="product-category-form">
                    <div>
                        <label htmlFor="category">Category: </label>
                        <select id="category" name="category" onChange={e => onSelectCategoryIdChange(e.target.value)} value={selectedCategoryId}>
                            <option name='' value=''>...</option>
                            {avaibleCategories.map(category => <option key={category.id} name={category.categoryName} value={category.id} id={category.id}>{category.categoryName}</option>)}
                        </select>
                    </div>

                    {avaibleSubCategories && 
                        <div>
                            <label htmlFor="subCategory">Sub category: </label>
                            <select id="subCategory" name="subCategory" onChange={e => onSelectedSubCategoryIdChange(e.target.value)} value={selectedSubcategoryId}>
                                <option name='' value=''>...</option>
                                {avaibleSubCategories.map(subCategory => <option key={subCategory.id} name={subCategory.subcategory} value={subCategory.id} id={subCategory.id}>{subCategory.subcategory}</option>)}
                            </select>
                        </div>
                    }
                </div>
        }
        </>
    )    
}

ProductCategoryForm.propTypes={
    onSelectCategoryIdChange: Proptypes.func,
    selectedCategoryId: Proptypes.string,
    selectedSubcategoryId: Proptypes.string,
    onSelectedSubCategoryIdChange: Proptypes.func
}

export default ProductCategoryForm;