import { useEffect, useState } from "react";
import Proptypes from "prop-types";
import { fetchGet } from "../../utility/fetchUtilities";
import ErrorMessageTitle from "../ErrorMessageTitle";

function ProductCategoryForm({onSelectCategoryIdChange, selectedCategoryId, selectedSubcategoryId, onSelectedSubCategoryIdChange}){
    const [avaibleCategories, setAvaibleCategories] = useState();
    const [avaibleSubCategories, setAvaibleSubCategories] = useState();
    const [errorMessage, setErrorMessage] = useState('')

    useEffect(() => {
        const getCategories = async () => {
            try{
                const categoryResponse = await fetchGet('/api/product/category');
                setAvaibleCategories(categoryResponse);
            } catch(error){
                console.log(error.message.errorMessage);
                setErrorMessage(error.message.errorMessage);
            }
        }

        getCategories();
    }, []);

    useEffect(() => {
        if(!selectedCategoryId){
            setAvaibleSubCategories('');
            onSelectedSubCategoryIdChange('');
        }

        const getSubCategories = async () => {
            try{
                const subCategoryResponse = await fetchGet(`/api/product/subcategory/${selectedCategoryId}`);
                setAvaibleSubCategories(subCategoryResponse);
            } catch(error){
                console.log(error.message.errorMessage);
                setErrorMessage(error.message.errorMessage);
            }
        }

        if(selectedCategoryId){
            getSubCategories();
        }
    }, [selectedCategoryId]);

    return(
        <>
        {errorMessage && <ErrorMessageTitle error={errorMessage}/>}
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