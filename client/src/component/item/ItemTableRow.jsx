import Proptypes from 'prop-types';

function ItemTableRow({productPropertyName, productPropertyValue}){
    return(
        <tr>
            <th>{productPropertyName}</th>
            <td>{productPropertyValue}</td>
        </tr>
    )
}

ItemTableRow.propTypes = {
    productPropertyName: Proptypes.string,
    productPropertyValue: Proptypes.any,
}

export default ItemTableRow;