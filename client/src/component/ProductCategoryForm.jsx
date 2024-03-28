import { useEffect, useState } from "react";
import Proptypes from "prop-types";

function ProductCategoryForm({user, onDetailSet, isChangedCategory, onCategoryChange}){
    const [avaibleCategories, setAvaibleCategories] = useState();
    const [subCategories, setSubCategories] = useState();
    const [selectedCategoryId, setSelectedCaegoryId] = useState();
    const [selectedSubcategoryId, setSelectedSubcategoryId] = useState();

    console.log(subCategories);


    useEffect(() => {
        const getCategories = async () => {
            const categoriesData = await fetch('/api/product/category');
            const categoriesDataResponse = await categoriesData.json();
            setAvaibleCategories(categoriesDataResponse);
        }

        getCategories();
    }, []);

    useEffect(() => {
        if(avaibleCategories && selectedCategoryId){
            const getSubCategories = async () => {
                const subCategoriesData = await fetch(`/api/product/subcategory/${selectedCategoryId}`);
                const subCategoriesResponse = await subCategoriesData.json();
                

                setSubCategories(subCategoriesResponse);
            }

            getSubCategories();
        } else{
            setSubCategories();
            setSelectedSubcategoryId();
        }
    }, [avaibleCategories, selectedCategoryId]);

    useEffect(() => {
        if(selectedSubcategoryId){
            const getDetails = async () => {
                const detailsData = await fetch(`/api/product/detail/${selectedSubcategoryId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${user.jwt}`
                    }
                });
                const details = await detailsData.json();

                const detailObj = {};
                details.forEach(detail => detailObj[detail.detailName] = '');
                onDetailSet(detailObj);                
            }

            getDetails();
        } else{
            onDetailSet(null);
        }
    }, [selectedSubcategoryId, user]);

    useEffect(() => {
        setSelectedCaegoryId();
        setSelectedSubcategoryId();
    }, [isChangedCategory]);

    return(
        <>
            {avaibleCategories && 
                <div className="product-category-form">
                    <label htmlFor="category">Category: </label>
                    <select onChange={e => setSelectedCaegoryId(e.target.value)} id="category" name="category">
                        <option name='' value=''>...</option>
                        {avaibleCategories.map(category => <option key={category.id} name={category.categoryName} value={category.id} id={category.id}>{category.categoryName}</option>)}
                    </select>
                    <br />
                    {subCategories &&
                        <>
                            <label htmlFor="subCategory">Sub category: </label>
                            <select id="subCategory" name="subCategory" onChange={(e) => {setSelectedSubcategoryId(e.target.value); onCategoryChange({'Category': selectedCategoryId, 'Subcategory': e.target.id})}}>
                                <option name='' value=''>...</option>
                                {subCategories.map(subCategory => <option key={subCategory.id} name={subCategory.subcategory} value={subCategory.id} id={subCategory.id}>{subCategory.subcategory}</option>)}
                            </select>
                        </>
                    }
                </div>
            }
        </>
    )
}

ProductCategoryForm.propTypes = {
    user: Proptypes.object,
    onDetailSet: Proptypes.func
}

export default ProductCategoryForm;