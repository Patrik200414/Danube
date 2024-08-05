import { useState } from "react";
import ProductContainer from "../component/product/ProductContainer";
import { useFetch } from "../utility/customHook/fetchHooks";
import ErrorMessage from "../component/ErrorMessage";

function ProductsPage(){
    const itemPerPage = 9;
    const [pageNumber, setPageNumber] = useState(0);
    const [pageProducts, , error] = useFetch(`/api/product?pageNumber=${pageNumber}&itemPerPage=${itemPerPage}`);


    return(
        <div className="home">
            {error && <ErrorMessage error={error}/>}
            <ProductContainer pageProducts={pageProducts} onPaginationNumberChange={(pageNumber) => setPageNumber(pageNumber)}/>
        </div>
    )
}

export default ProductsPage;