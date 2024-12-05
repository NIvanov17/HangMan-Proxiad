import React from 'react';
import Footer from '../Components/Footer/Footer';

export default {
    title: 'Components/Footer',
    component: Footer
};

const Template = (args) => <Footer{...args} />;

export const Default = Template.bind({});
Default.args = {};