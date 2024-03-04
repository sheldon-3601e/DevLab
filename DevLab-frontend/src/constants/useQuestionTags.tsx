import { useState, useEffect } from 'react';
import { getQuestionTagsUsingGet } from '@/services/backend/questionController';

interface TagsOptionsType {
  label: string;
  value: string;
}

const useQuestionTags = () => {
  const [tags, setTags] = useState<TagsOptionsType[]>([]);

  useEffect(() => {
    const fetchQuestionTags = async () => {
      const result = await getQuestionTagsUsingGet();
      if (result.data) {
        const tagsOptions = result.data.map((tag) => ({
          label: tag.tagName ?? '',
          value: tag.tagName ?? '',
        }));
        setTags(tagsOptions);
      }
    };

    fetchQuestionTags();
  }, []);

  return tags;
};

export default useQuestionTags;
