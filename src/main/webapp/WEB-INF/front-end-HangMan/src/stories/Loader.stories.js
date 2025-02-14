import React from 'react';
import Loader from '../Components/Loader/Loader';

export default {
    title: 'Components/Loader',
    component: Loader
};

const Template = (args) => <Loader {...args} />;
export const Default = Template.bind({});
Default.args = {};