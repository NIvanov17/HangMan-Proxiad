import React from 'react';
import PaginationComponent from '../Components/Pagination/PaginationComponent';

export default {
    title: 'Components/PaginationComponent',
    component: PaginationComponent,
};

const Template = (args) => <PaginationComponent {...args} />

export const Default = Template.bind({});
Default.args = {
    currentPage: 2,
    totalPages: 10,
    onPageChange: (page) => console.log('Page changed to:', page)
};

export const FirstPage = Template.bind({});
FirstPage.args = {
    currentPage: 0,
    totalPages: 10,
    onPageChange: (page) => console.log('Page changed to:', page)
};

export const LastPage = Template.bind({});
LastPage.args = {
    currentPage: 9,
    totalPages: 10,
    onPageChange: (page) => console.log('Page changed to:', page)
};

export const FewPages = Template.bind({});
FewPages.args = {
    currentPage: 1,
    totalPages: 3,
    onPageChange: (page) => console.log('Page changed to:' + page)
};

