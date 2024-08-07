import styled from "styled-components";

const StyledCarouselContainer = styled('div')({
    maxWidth: '200px',
    maxHeight: '200px',
    display: 'flex',
    justifyContent: 'center',
    margin: 'auto',
    marginTop: '15px',
    gap: '10px',
    flexWrap: 'wrap'
});

const StyledCarousel = styled('div')({
    height: '10px',
    border: '1px solid gray'
});

export default function Carousel({ images, currSeledIndex, onCarouselClick }) {
    return (
        <StyledCarouselContainer>
            {images.map((image, i) => <StyledCarousel
                style={{
                    backgroundColor: currSeledIndex === i ? '#D3D3D3' : 'white',
                    width: currSeledIndex === i ? '20px' : '10px',
                    borderRadius: currSeledIndex === i ? '30%' : '50%'
                }}
                key={`${image}-${i}`}
                onClick={() => onCarouselClick(i)}
            />
            )}
        </StyledCarouselContainer>
    )
}