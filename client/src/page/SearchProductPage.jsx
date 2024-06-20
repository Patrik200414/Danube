import { useEffect, useState } from "react";
import ProductContainer from "../component/product/ProductContainer";
import { useParams } from "react-router-dom";

function searchedProductPage(){
    const itemPerPage = 9;
    const [pageNumber, setPageNumber] = useState(0);
    const [error, setError] = useState();
    const [pageProducts, setPageProducts] = useState();
    const {subcategoryName} = useParams();

    useEffect(() => {
        const getProduct = async () => {
            const getProductsBySubcategory = await fetch(`/api/search/product/item?subcategoryName=${subcategoryName}`);
            const getProductsBySubcategoryResponse = await getProductsBySubcategory.json();

            if(getProductsBySubcategory.ok){
                setPageProducts(getProductsBySubcategoryResponse);
                setError();
            } else{
                setError(getProductsBySubcategoryResponse.errorMessage);
            }
        }

        if(subcategoryName){
            getProduct()
        }
    }, [subcategoryName])

    return(
        <div className="home">
            {error ? 
                <p className="error-message">{error}</p> 
                :
                <ProductContainer pageProducts={pageProducts} onPaginationNumberChange={(pageNumber) => setPageNumber(pageNumber)}/>
            }
            {(pageProducts && pageProducts.products < 1) && <h1 className="searched-product-message">The searched category doesn't exists!</h1>}                
        </div>
    )
}

export default searchedProductPage;