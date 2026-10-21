
import Swiper from 'swiper';
import { Navigation, Pagination, A11y } from 'swiper/modules';

function initCarousels() {
    document.querySelectorAll('.cmp-carousel.swiper').forEach((element) => {
        if (element.swiper) {
            return;
        }

        const wrapper = element.querySelector('.swiper-wrapper');
        if (!wrapper) {
            return;
        }

        new Swiper(element, {
            modules: [Navigation, Pagination, A11y],
            slidesPerView: 1,
            slidesPerGroup: 1,
            spaceBetween: 20,
            loop: false,
            speed: 600,
            navigation: {
                nextEl: element.querySelector('[data-cmp-hook-carousel="next"]'),
                prevEl: element.querySelector('[data-cmp-hook-carousel="previous"]')
            },
            pagination: {
                el: element.querySelector('.swiper-pagination'),
                clickable: true
            },
            a11y: {
                enabled: true
            }
        });
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initCarousels);
} else {
    initCarousels();
}

