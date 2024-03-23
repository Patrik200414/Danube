import { useEffect, useState } from "react";

function ProductCategoryForm(){
    const [avaibleCategories, setAvaibleCategories] = useState();
    const [subCategories, setSubCategories] = useState();
    const [selectedCategory, setSelectedCaegory] = useState();

    useEffect(() => {
        const getCategories = async () => {
            const categoriesData = await fetch('/api/product/category');
            const categoriesDataResponse = await categoriesData.json();
            setAvaibleCategories(categoriesDataResponse);
        }

        getCategories();
    }, []);

    useEffect(() => {
        if(avaibleCategories && selectedCategory){
            const getSubCategories = async () => {
                const subCategoriesData = await fetch(`/api/product/subcategory/${selectedCategory}`);
                const subCategoriesResponse = await subCategoriesData.json();
                console.log(subCategoriesResponse);

                setSubCategories(subCategoriesResponse);
            }

            getSubCategories();
        } else{
            setSubCategories();
        }
    }, [avaibleCategories, selectedCategory]);

    return(
        <>
            {avaibleCategories && 
                <div className="product-category-form">
                    <label htmlFor="category">Category: </label>
                    <select onChange={e => setSelectedCaegory(e.target.value)} id="category" name="category">
                        <option name='' value=''>...</option>
                        {avaibleCategories.map(category => <option key={category.categoryName} name={category.categoryName} value={category.categoryName} id={category.categoryName}>{category.categoryName}</option>)}
                    </select>
                    <br />
                    {subCategories &&
                        <>
                            <label htmlFor="subCategory">Sub category: </label>
                            <select id="subCategory" name="subCategory">
                                <option name='' value=''>...</option>
                                {subCategories.map(subCategory => <option key={subCategory.subcategory} name={subCategory.subcategory} value={subCategory.subcategory} id={subCategory.subcategory}>{subCategory.subcategory}</option>)}
                            </select>
                        </>
                    }
                </div>
            }
        </>
    )
}

export default ProductCategoryForm;